package com.hit.memoryunits;

import java.io.Serializable;

public class Page<T> implements Serializable
{
	private static final long serialVersionUID = -110306986780244913L;
	private Long m_pageId;
	private T m_content;
	
	public Page(Long pageId, T content)
	{
		m_pageId = pageId;
		m_content = content;
	}
	
	public Long getPageId()
	{
		return m_pageId;
	}
	
	public void setPageId(Long pageId)
	{
		m_pageId = pageId;
	}
	
	public T getContent()
	{
		return m_content;
	}
	
	public void setContent(T content)
	{
		m_content = content;
	}
	
	@Override
	public int hashCode()
	{
		return m_pageId.hashCode();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		boolean isEquals = false;

		if (this == obj || m_pageId == ((Page<T>)obj).getPageId())
		{
			isEquals = true;
		}
		
		return isEquals;
	}
	
	public String toString()
	{
		return m_pageId.toString();
	}
}
