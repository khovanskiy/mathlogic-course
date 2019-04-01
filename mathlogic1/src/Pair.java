
public class Pair<T,S>
{
	private T first;
	private S second;
	
	public Pair(T first, S second)
	{
		this.first = first;
		this.second = second;
	}
	
	public T getFirst()
	{
		return this.first;
	}
	
	public S getSecond()
	{
		return this.second;
	}
}
