package ru.disdev.jdbchelper;

import java.util.Iterator;


/**
 * Author: Erdinc YILMAZEL
 * Date: Mar 12, 2009
 * Time: 2:39:16 PM
 */
public class Tuple<X, Y> implements Iterable
{
	final X x;
	final Y y;

	public Tuple(X x, Y y)
	{
		this.x = x;
		this.y = y;
	}

	public X getFirst()
	{
		return x;
	}

	public Y getSecond()
	{
		return y;
	}

	public Object get(int index)
	{
		switch(index)
		{
			case 0:
				return x;
			case 1:
				return y;
			default:
				throw new IndexOutOfBoundsException("Undefined index " + index + " for a Tuple");
		}
	}

	public int size()
	{
		return 2;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;

		Tuple tuple = (Tuple) o;

		return !(x != null ? !x.equals(tuple.x) : tuple.x != null) &&
		       !(y != null ? !y.equals(tuple.y) : tuple.y != null);
	}

	@Override
	public int hashCode()
	{
		int result = x != null ? x.hashCode() : 0;
		result = 31 * result + (y != null ? y.hashCode() : 0);
		return result;
	}

	class TupleIterator implements Iterator
	{
		private int index = 0;

		public boolean hasNext()
		{
			return index != size();
		}

		public Object next()
		{
			return get(index++);
		}

		public void remove()
		{
		}
	}

	public Iterator iterator()
	{
		return new TupleIterator();
	}
}
