package Utility;

import java.util.Comparator;

import com.zevenpooja.attini.News;

public class ViewsComparator implements Comparator<News>
{

	@Override
	public int compare(News lhs, News rhs)
	{
		// TODO Auto-generated method stub
		 
		return  (Integer.valueOf(rhs.getNewsViews()) -Integer.valueOf(lhs.getNewsViews()));
	}

	

}

