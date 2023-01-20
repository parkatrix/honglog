package com.honglog.api.repository;

import com.honglog.api.domain.Post;
import com.honglog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {


    List<Post> getList(PostSearch postSearch);



}
