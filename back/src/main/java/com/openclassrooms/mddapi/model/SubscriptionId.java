package com.openclassrooms.mddapi.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SubscriptionId implements Serializable {
    private Long userId;
    private Long subjectId;

    public SubscriptionId() {}
    public SubscriptionId(Long userId, Long subjectId) {
        this.userId = userId; this.subjectId = subjectId;
    }

    public Long getUserId() { return userId; }
    public Long getSubjectId() { return subjectId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionId)) return false;
        SubscriptionId that = (SubscriptionId) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(subjectId, that.subjectId);
    }
    @Override public int hashCode() { return Objects.hash(userId, subjectId); }
}
