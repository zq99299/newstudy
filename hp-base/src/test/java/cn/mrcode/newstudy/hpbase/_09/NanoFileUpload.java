package cn.mrcode.newstudy.hpbase._09;

/*
 * #%L
 * NanoHttpd-apache file upload integration
 * %%
 * Copyright (C) 2012 - 2016 nanohttpd
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the nanohttpd nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import org.apache.commons.fileupload.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author victor & ritchieGitHub
 */
public class NanoFileUpload extends FileUpload {

    public static class NanoHttpdContext implements UploadContext {

        private Request request;

        public NanoHttpdContext(Request request) {
            this.request = request;
        }

        @Override
        public long contentLength() {
            long size;
            try {
                String cl1 = request.getHeaders().get("Content-Length");
                size = Long.parseLong(cl1);
            } catch (NumberFormatException var4) {
                size = -1L;
            }

            return size;
        }

        @Override
        public String getCharacterEncoding() {
            return "UTF-8";
        }

        @Override
        public String getContentType() {
            return this.request.getHeaders().get("Content-Type");
        }

        @Override
        public int getContentLength() {
            return (int) contentLength();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return request.getInputStream();
        }
    }

    public static final boolean isMultipartContent(Request request) {
        return request.getMethod() == Method.POST && FileUploadBase.isMultipartContent(new NanoHttpdContext(request));
    }

    public NanoFileUpload(FileItemFactory fileItemFactory) {
        super(fileItemFactory);
    }

    public List<FileItem> parseRequest(Request request) throws FileUploadException {
        return this.parseRequest(new NanoHttpdContext(request));
    }

    public Map<String, List<FileItem>> parseParameterMap(Request request) throws FileUploadException {
        return this.parseParameterMap(new NanoHttpdContext(request));
    }

    public FileItemIterator getItemIterator(Request request) throws FileUploadException, IOException {
        return super.getItemIterator(new NanoHttpdContext(request));
    }

}
