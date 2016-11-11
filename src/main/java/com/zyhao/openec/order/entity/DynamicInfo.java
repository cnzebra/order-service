package com.zyhao.openec.order.entity;

import java.io.Serializable;

/**
 * Model class of 动态信息表.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class DynamicInfo implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** 主键ID. */
	private Long id;

	/** 标题. */
	private String title;

	/** 内容. */
	private String content;

	/** 创建时间. */
	private Long createdAt;

	/** 关联关系id. */
	private String relationId;

	/** 信息类型. */
	private String type;

	/** 用户id. */
	private Long memberId;

	/**
	 * Constructor.
	 */
	public DynamicInfo() {
	}

	/**
	 * Set the 主键ID.
	 * 
	 * @param id
	 *            主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the 主键ID.
	 * 
	 * @return 主键ID
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the 标题.
	 * 
	 * @param title
	 *            标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the 标题.
	 * 
	 * @return 标题
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Set the 内容.
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Get the 内容.
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Set the 创建时间.
	 * 
	 * @param createdAt
	 *            创建时间
	 */
	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Get the 创建时间.
	 * 
	 * @return 创建时间
	 */
	public Long getCreatedAt() {
		return this.createdAt;
	}

	/**
	 * Set the 关联关系id.
	 * 
	 * @param relationId
	 *            关联关系id
	 */
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	/**
	 * Get the 关联关系id.
	 * 
	 * @return 关联关系id
	 */
	public String getRelationId() {
		return this.relationId;
	}

	/**
	 * Set the 信息类型.
	 * 
	 * @param type
	 *            信息类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get the 信息类型.
	 * 
	 * @return 信息类型
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Set the 用户id.
	 * 
	 * @param memberId
	 *            用户id
	 */
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	/**
	 * Get the 用户id.
	 * 
	 * @return 用户id
	 */
	public Long getMemberId() {
		return this.memberId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DynamicInfo other = (DynamicInfo) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
