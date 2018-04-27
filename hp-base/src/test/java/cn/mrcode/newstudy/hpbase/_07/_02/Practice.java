package cn.mrcode.newstudy.hpbase._07._02;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *
 * 用线程池框架或者fork-jion框架实现一个并发的文件内容查找接口：
 * public SearchResult searchInFiles(String key);
 * 查询指定目录下的所有txt或者java文件（建议查找Java工程文件）
 * 目录递归最多为4层，即从根目录开始，最多3层子目录中的文件搜索
 * 每个文件中如果出现关键字，则关键字的次数+1，并且将此文件的路径也保持到List中
 * 文件中出现关键字最多次的文件排名第一，以此类推：
 * 屏幕最后输出：
 * xxx总共出现N次，
 * 其中 2次出现在yyy文件中
 * 3次出现在xxx文件中
 * </pre>
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/4/25 22:24
 */
public class Practice {
    ForkJoinPool pool = new ForkJoinPool();

    @Test
    public void test() {
        searchInFiles("test").print();
        pool.shutdown();
    }

    public SearchResult searchInFiles(String key) {
        int maxDepth = 8;
        Path root = Paths.get("f:\\dev\\project\\newstudy\\hp-base\\src");
        List<Path> paths = collectPath(root, maxDepth);

        SubTask task = new SubTask(paths, 0, paths.size(), key);
        pool.execute(task);
        try {
            List<Result> results = task.get();
            SearchResult searchResult = new SearchResult(results);
            searchResult.setKey(key);
            searchResult.setMaxDepth(maxDepth);
            searchResult.setRootPath(root);
            searchResult.setTotalCount(results.stream().mapToInt(Result::getCount).sum());
            return searchResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有要查找的目标文件
     * @param root
     * @param maxDepth
     * @return
     */
    public List<Path> collectPath(Path root, int maxDepth) {
        if (maxDepth < 0) {
            return null;
        }
        List<Path> result = new ArrayList<>();
        try {
            Files.list(root).forEach(path -> {
                if (Files.isDirectory(path)) {
                    List<Path> paths = collectPath(path, maxDepth - 1);
                    if (paths != null) {
                        result.addAll(paths);
                    }
                } else {
                    result.add(path);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public class SubTask extends RecursiveTask<List<Result>> {
        private List<Path> list;
        private int beg;
        private int end;
        private String key;

        public SubTask(List<Path> list, int beg, int end, String key) {
            this.list = list;
            this.beg = beg;
            this.end = end;
            this.key = key;
        }

        @Override
        protected List<Result> compute() {
            List<Result> result = new ArrayList<>();
            if (end - beg > 2) {
                int mid = (beg + end) / 2;
                SubTask t1 = new SubTask(list, beg, mid, key);
                SubTask t2 = new SubTask(list, mid, end, key);
                invokeAll(t1, t2);
                try {
                    result.addAll(t1.get());
                    result.addAll(t2.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                result = count();
            }
            return result;
        }

        private List<Result> count() {
            Pattern p = Pattern.compile(key);
            List<Result> files = new ArrayList<>();
            for (int i = beg; i < end; i++) {
                Path path = list.get(i);
                try (
                        BufferedReader reader = Files.newBufferedReader(path);
                ) {
                    int sum = reader.lines()
                            .mapToInt(line -> {
                                int count = 0;
                                Matcher matcher = p.matcher(line);
                                while (matcher.find()) {
                                    count++;
                                }
                                return count;
                            }).sum();
                    if (sum != 0) {
                        files.add(new Result(path, sum));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return files;
        }
    }

    public class SearchResult {
        private List<Result> result;
        private String key;
        private int maxDepth;
        private Path rootPath;
        private int totalCount;

        public SearchResult(List<Result> result) {
            this.result = result;
        }

        public SearchResult addAll(List<Result> result) {
            this.result.addAll(result);
            return this;
        }

        public void print() {
            System.out.println("关键词：" + key);
            System.out.println("rootPath：" + rootPath);
            System.out.println("maxDepth：" + maxDepth);
            System.out.println("总出现：" + totalCount + "次");
            result
                    .stream()
                    .sorted(Comparator.comparing(Result::getCount).reversed())
                    .forEach(System.out::println);
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setMaxDepth(int maxDepth) {
            this.maxDepth = maxDepth;
        }

        public void setRootPath(Path rootPath) {
            this.rootPath = rootPath;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }

    public static class Result {
        private Path target;
        private int count;

        public Result(Path target, int count) {
            this.target = target;
            this.count = count;
        }

        public boolean isEmpty() {
            return target == null;
        }

        public Path getTarget() {
            return target;
        }

        public void setTarget(Path target) {
            this.target = target;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "count=" + count +
                    ", target=" + target +
                    '}';
        }
    }
}
