package io.github.susimsek.springkafkasamples.client;

import io.github.susimsek.springkafkasamples.dto.PostDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jplaceholder", url = "https://jsonplaceholder.typicode.com/")
public interface JSONPlaceHolderClient {

    @GetMapping("/posts")
    List<PostDTO> getPosts();
}