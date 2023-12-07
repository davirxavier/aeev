package com.davixavier.nodes;

public class SearchBarOptionsItem
{
	private Runnable optionRunnable;
	private String runnableText;
	
	public SearchBarOptionsItem() 
	{
		optionRunnable = () -> {};
		runnableText = "";
	}
	
	public SearchBarOptionsItem(String runnableText)
	{
		this.runnableText = runnableText;
	}
	
	public SearchBarOptionsItem(Runnable optionRunnable, String runnableText)
	{
		this.optionRunnable = optionRunnable;
		this.runnableText = runnableText;
	}
	
	public Runnable getOptionRunnable()
	{
		return optionRunnable;
	}
	public void setOptionRunnable(Runnable optionRunnable)
	{
		this.optionRunnable = optionRunnable;
	}
	public String getRunnableText()
	{
		return runnableText;
	}
	public void setRunnableText(String runnableText)
	{
		this.runnableText = runnableText;
	}
	
	public String toString()
	{
		return runnableText;
	}
}
