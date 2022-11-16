package com.honglog.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;

    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static PostCreateBuilder builder() {
        return new PostCreateBuilder();
    }

    public static class PostCreateBuilder {
        private String title;
        private String content;

        PostCreateBuilder() {
        }

        public PostCreateBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PostCreateBuilder content(String content) {
            this.content = content;
            return this;
        }

        public PostCreate build() {
            return new PostCreate(title, content);
        }

        public String toString() {
            return "PostCreate.PostCreateBuilder(title=" + this.title + ", content=" + this.content + ")";
        }
    }
}
