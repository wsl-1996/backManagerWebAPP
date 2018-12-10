package com.skqtec.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "COMMENT", schema = "ketuanDB_test", catalog = "")
public class CommentEntity {
    private String id;
    private String commentContent;
    private String attachImgs;
    private String userId;
    private String productId;
    private String groupId;
    private int starLevel;

    public void setStarLevel(Integer starLevel) {
        this.starLevel = starLevel;
    }

    private String reply;
    private String evaluateLabel;
    private Timestamp commentTime;
    private String productStyle;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "comment_content")
    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @Basic
    @Column(name = "attach_imgs")
    public String getAttachImgs() {
        return attachImgs;
    }

    public void setAttachImgs(String attachImgs) {
        this.attachImgs = attachImgs;
    }

    @Basic
    @Column(name = "USER_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "PRODUCT_id")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "GROUP_id")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Basic
    @Column(name = "star_level")
    public int getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(int starLevel) {
        this.starLevel = starLevel;
    }

    @Basic
    @Column(name = "reply")
    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    @Basic
    @Column(name = "evaluate_label")
    public String getEvaluateLabel() {
        return evaluateLabel;
    }

    public void setEvaluateLabel(String evaluateLabel) {
        this.evaluateLabel = evaluateLabel;
    }

    @Basic
    @Column(name = "comment_time")
    public Timestamp getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Timestamp commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentEntity that = (CommentEntity) o;

        if (starLevel != that.starLevel) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (commentContent != null ? !commentContent.equals(that.commentContent) : that.commentContent != null)
            return false;
        if (attachImgs != null ? !attachImgs.equals(that.attachImgs) : that.attachImgs != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (productId != null ? !productId.equals(that.productId) : that.productId != null) return false;
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (reply != null ? !reply.equals(that.reply) : that.reply != null) return false;
        if (evaluateLabel != null ? !evaluateLabel.equals(that.evaluateLabel) : that.evaluateLabel != null)
            return false;
        if (commentTime != null ? !commentTime.equals(that.commentTime) : that.commentTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (commentContent != null ? commentContent.hashCode() : 0);
        result = 31 * result + (attachImgs != null ? attachImgs.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + starLevel;
        result = 31 * result + (reply != null ? reply.hashCode() : 0);
        result = 31 * result + (evaluateLabel != null ? evaluateLabel.hashCode() : 0);
        result = 31 * result + (commentTime != null ? commentTime.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "product_style")
    public String getProductStyle() {
        return productStyle;
    }

    public void setProductStyle(String productStyle) {
        this.productStyle = productStyle;
    }
}
