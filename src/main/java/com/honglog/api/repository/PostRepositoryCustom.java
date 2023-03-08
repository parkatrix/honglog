package com.honglog.api.repository;

import com.honglog.api.domain.Post;
import com.honglog.api.request.PostSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepositoryCustom {


    List<Post> getList(PostSearch postSearch);



}
