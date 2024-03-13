package com.myfarmblog.farmnews.payload.response;

import com.myfarmblog.farmnews.payload.requests.PostRequest;
import lombok.*;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private List<PostRequest>content;
    private int pageNo;
    private int pageSize;
    private long totalElement;
    private int totalPage;
    private boolean last;
}
