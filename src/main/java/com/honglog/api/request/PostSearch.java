package com.honglog.api.request;


import static java.lang.Math.max;
import static java.lang.Math.min;

public class PostSearch {

    private static final int MAX_SIZE = 2000;

    private Integer page = 1;

    private Integer size = 10;

    public PostSearch(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public PostSearch() {
    }

    private static Integer $default$page() {
        return 1;
    }

    private static Integer $default$size() {
        return 10;
    }

    public static PostSearchBuilder builder() {
        return new PostSearchBuilder();
    }

    public long getOffset() {
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }

    public Integer getPage() {
        return this.page;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public static class PostSearchBuilder {
        private Integer page$value;
        private boolean page$set;
        private Integer size$value;
        private boolean size$set;

        PostSearchBuilder() {
        }

        public PostSearchBuilder page(Integer page) {
            this.page$value = page;
            this.page$set = true;
            return this;
        }

        public PostSearchBuilder size(Integer size) {
            this.size$value = size;
            this.size$set = true;
            return this;
        }

        public PostSearch build() {
            Integer page$value = this.page$value;
            if (!this.page$set) {
                page$value = PostSearch.$default$page();
            }
            Integer size$value = this.size$value;
            if (!this.size$set) {
                size$value = PostSearch.$default$size();
            }
            return new PostSearch(page$value, size$value);
        }

        public String toString() {
            return "PostSearch.PostSearchBuilder(page$value=" + this.page$value + ", size$value=" + this.size$value + ")";
        }
    }
}


