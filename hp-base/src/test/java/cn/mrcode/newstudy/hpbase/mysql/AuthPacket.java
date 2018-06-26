/*
 * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software;Designed and Developed mainly by many Chinese
 * opensource volunteers. you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 2 only, as published by the
 * Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Any questions about this component can be directed to it's project Web address
 * https://code.google.com/p/opencloudb/.
 *
 */
package cn.mrcode.newstudy.hpbase.mysql;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * From client to server during initial handshake.
 * <p>
 * <pre>
 * Bytes                        Name
 * -----                        ----
 * 4                            client_flags
 * 4                            max_packet_size
 * 1                            charset_number
 * 23                           (filler) always 0x00...
 * n (Null-Terminated String)   user
 * n (Length Coded Binary)      scramble_buff (1 + x bytes)
 * n (Null-Terminated String)   databasename (optional)
 *
 * @see http://forge.mysql.com/wiki/MySQL_Internals_ClientServer_Protocol#Client_Authentication_Packet
 * </pre>
 * @author mycat
 */
public class AuthPacket extends MySQLPacket {
    private static final byte[] FILLER = new byte[23];

    public long clientFlags;
    public long maxPacketSize;
    public int charsetIndex;
    public byte[] extra;// from FILLER(23)
    public String user;
    public byte[] password;
    public String database;

    private ConcurrentLinkedQueue<ByteBuffer> writeQueue;

    public AuthPacket(ConcurrentLinkedQueue<ByteBuffer> writeQueue) {
        this.writeQueue = writeQueue;
    }

    public void write() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16 * 1024 * 1024);
        BufferUtil.writeUB3(buffer, calcPacketSize());
        buffer.put(packetId);
        BufferUtil.writeUB4(buffer, clientFlags);
        BufferUtil.writeUB4(buffer, maxPacketSize);
        buffer.put((byte) charsetIndex);
        buffer = writeToBuffer(FILLER, buffer);
        if (user == null) {
            buffer = checkWriteBuffer(buffer, 1, true);
            buffer.put((byte) 0);
        } else {
            byte[] userData = user.getBytes();
            buffer = checkWriteBuffer(buffer, userData.length + 1, true);
            BufferUtil.writeWithNull(buffer, userData);
        }
        if (password == null) {
            buffer = checkWriteBuffer(buffer, 1, true);
            buffer.put((byte) 0);
        } else {
            buffer = checkWriteBuffer(buffer, BufferUtil.getLength(password), true);
            BufferUtil.writeWithLength(buffer, password);
        }
        if (database == null) {
            buffer = checkWriteBuffer(buffer, 1, true);
            buffer.put((byte) 0);
        } else {
            byte[] databaseData = database.getBytes();
            buffer = checkWriteBuffer(buffer, databaseData.length + 1, true);
            BufferUtil.writeWithNull(buffer, databaseData);
        }
        writeQueue.offer(buffer);
    }

    public ByteBuffer writeToBuffer(byte[] src, ByteBuffer buffer) {
        int offset = 0;
        int length = src.length;
        int remaining = buffer.remaining();
        while (length > 0) {
            if (remaining >= length) {
                buffer.put(src, offset, length);
                break;
            } else {
                buffer.put(src, offset, remaining);
                writeNotSend(buffer);
                buffer = ByteBuffer.allocate(16 * 1024 * 1024);
                offset += remaining;
                length -= remaining;
                remaining = buffer.remaining();
                continue;
            }
        }
        return buffer;
    }

    private void writeNotSend(ByteBuffer buffer) {
        writeQueue.offer(buffer);
    }

    public ByteBuffer checkWriteBuffer(ByteBuffer buffer, int capacity, boolean writeSocketIfFull) {
        if (capacity > buffer.remaining()) {
            if (writeSocketIfFull) {
                writeNotSend(buffer);
                return ByteBuffer.allocate(16 * 1024 * 1024);
            } else {// Relocate a larger buffer
                buffer.flip();
                ByteBuffer newBuf = ByteBuffer.allocate(16 * 1024 * 1024);
                newBuf.put(buffer);
                return newBuf;
            }
        } else {
            return buffer;
        }
    }

    @Override
    public int calcPacketSize() {
        int size = 32;// 4+4+1+23;
        size += (user == null) ? 1 : user.length() + 1;
        size += (password == null) ? 1 : BufferUtil.getLength(password);
        size += (database == null) ? 1 : database.length() + 1;
        return size;
    }

    @Override
    protected String getPacketInfo() {
        return "MySQL Authentication Packet";
    }

}