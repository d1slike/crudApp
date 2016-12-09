package ru.disdev.jdbchelper;

/**
 * Author: Erdinc YILMAZEL
 * Date: Mar 12, 2009
 * Time: 2:40:03 PM
 */
public class Triple<X, Y, Z> extends Tuple<X, Y>
{
	final Z z;

	public Triple(X x, Y y, Z z)
	{
		super(x, y);
		this.z = z;
	}

	public Z getThird()
	{
		return z;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		if(!super.equals(o))
			return false;

		Triple triple = (Triple) o;

		return !(z != null ? !z.equals(triple.z) : triple.z != null);

	}

	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + (z != null ? z.hashCode() : 0);
		return result;
	}

	@Override
	public Object get(int index)
	{
		switch(index)
		{
			case 0:
				return x;
			case 1:
				return y;
			case 2:
				return z;
			default:
				throw new IndexOutOfBoundsException("Undefined index " + index + " for a Triple");
		}
	}

	@Override
	public int size()
	{
		return 3;
	}
}
