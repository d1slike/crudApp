package ru.disdev.jdbchelper;

/**
 * Author: Erdinc YILMAZEL
 * Date: Mar 12, 2009
 * Time: 2:42:33 PM
 */
public class Quadruple<X, Y, Z, W> extends Triple<X, Y, Z>
{
	final W w;

	public Quadruple(X x, Y y, Z z, W w)
	{
		super(x, y, z);
		this.w = w;
	}

	public W getFourth()
	{
		return w;
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

		Quadruple quadruple = (Quadruple) o;

		return !(w != null ? !w.equals(quadruple.w) : quadruple.w != null);

	}

	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + (w != null ? w.hashCode() : 0);
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
			case 3:
				return w;
			default:
				throw new IndexOutOfBoundsException("Undefined index " + index + " for a Quadruple");
		}
	}

	@Override
	public int size()
	{
		return 4;
	}
}
