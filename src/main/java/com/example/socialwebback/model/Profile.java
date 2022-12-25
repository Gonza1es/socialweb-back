package com.example.socialwebback.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String status;

    private String alias;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private Avatar avatar;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cover_id", referencedColumnName = "id")
    private Cover cover;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
                mappedBy = "profile")
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "profile")
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "profile_subscriptions",
            joinColumns = {@JoinColumn(name = "subscriber_id") },
            inverseJoinColumns = {@JoinColumn(name = "channel_id") }
    )
    @ToString.Exclude
    private Set<Profile> subscriptions = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "profile_subscriptions",
            joinColumns = {@JoinColumn(name = "channel_id") },
            inverseJoinColumns = {@JoinColumn(name = "subscriber_id") }
    )
    @ToString.Exclude
    private Set<Profile> subscribers = new HashSet<>();


    public void addAvatar(Avatar avatar) {
        setAvatar(avatar);
        avatar.setProfile(this);
    }

    public void addCover(Cover cover) {
        setCover(cover);
        cover.setProfile(this);
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setProfile(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Profile profile = (Profile) o;
        return id != null && Objects.equals(id, profile.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}