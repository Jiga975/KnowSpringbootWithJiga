package com.myfarmblog.farmnews.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
