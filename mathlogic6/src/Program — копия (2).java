import java.util.Arrays;


public class Program
{
	static void trace(String s)
	{
		System.out.println(s);
	}
	
	static void trace(int... array)
	{
		for (int i = 0; i < array.length; ++i)
		{
			System.out.print(array[i]+",");
		}
		System.out.println();
	}
	
	static interface IPrimitive
	{
		public int exec(int... array);
	}
	
	static class Zsample implements IPrimitive
	{
		@Override
		public int exec(int... array)
		{
			return 0;
		}
	}
	
	static IPrimitive Z = new Zsample();
	
	static class Nsample implements IPrimitive
	{
		@Override
		public int exec(int... array)
		{
			return array[0] + 1;
		}
	}
	
	static IPrimitive N = new Nsample();
	
	static class U implements IPrimitive
	{
		private int n;
		private int i;
		
		public U(int n, int i)
		{
			this.n = n;
			this.i = i;
		}
		
		@Override
		public int exec(int... array)
		{
			return array[i];
		}
	}
	
	static IPrimitive U_1 = new U(1, 0);
	static IPrimitive U_2 = new U(2, 1);
	static IPrimitive U_3 = new U(3, 2);
	static IPrimitive U_4 = new U(4, 3);
	
	static class R implements IPrimitive
	{
		private IPrimitive f;
		private IPrimitive g;
		
		public R(IPrimitive f, IPrimitive g)
		{
			this.f = f;
			this.g = g;
		}
		
		@Override
		public int exec(int... array)
		{
			int y = array[array.length - 1];
			int[] argc = Arrays.copyOfRange(array, 0, array.length - 1);
			int result = f.exec(argc);
			if (y > 0)
			{
				int[] temp = new int[argc.length + 2];
				System.arraycopy(argc, 0, temp, 0, argc.length);
				for (int i = 0; i < y; ++i)
				{
					temp[temp.length - 2] = i;
					temp[temp.length - 1] = result;
					result = g.exec(temp);
				}
			}
			return result;
		}
	}
	
	static class M implements IPrimitive
	{
		private IPrimitive f;
		
		public M(IPrimitive f)
		{
			this.f = f;
		}
		
		@Override
		public int exec(int... array)
		{
			int[] temp = new int[array.length + 1];
			System.arraycopy(array, 0, temp, 0, array.length);
			temp[temp.length - 1] = 0;
			trace(temp);
			while (f.exec(temp) != 0)
			{
				++temp[temp.length - 1];
			}
			return temp[temp.length - 1];
		}
	}
	
	static class S implements IPrimitive
	{
		private IPrimitive f;
		private IPrimitive[] gs;
		
		public S(IPrimitive f, IPrimitive... gs)
		{
			this.f = f;
			this.gs = gs.clone();
		}

		@Override
		public int exec(int... array)
		{
			int[] temp = new int[gs.length];
			for (int i = 0; i < gs.length; ++i)
			{
				temp[i] = gs[i].exec(array);
			}
			return f.exec(temp);
		}
	}
	
	static IPrimitive Sum = new R(U_1, new S(N, U_3));
	
	static IPrimitive Mul = new R(Z, new S(Sum, U_1, U_3));

	static IPrimitive Minus1 = new R(Z, U_1);

	static IPrimitive Minus = new R(U_1, new S(Minus1, U_3));
	
	static IPrimitive Div = new S(Minus1, new M(new S(Minus, new S(N, U_1), new S(Mul, U_2, U_3))));
	
	static IPrimitive Mod = new S(Minus, U_1, new S(Mul, U_2, new S(Div, U_1, U_2)));
	
	static IPrimitive One = new S(N, Z);

	static IPrimitive Or = new R(new S(new R( Z, One), U_1, U_1), new S(One, U_1));

	static IPrimitive And = new R(Z, new S(new R(Z, One),U_1,U_1));

	static IPrimitive Not = new R(One, Z);

	static IPrimitive If = new R(Z, One);

	static IPrimitive isPrime = new S(And, new S(Not, new S(new R(Z, new S(Or, new S(And, new S(Not, new S(Mod, U_1, new S(N,U_2))), new S(If, U_2) ), U_3) ),U_1, new S(Minus1, new S(Minus1,U_1)))), new S(If, new S(Minus1,U_1)));

	static IPrimitive kolPrime = 	new R(Z, new S(Sum, new S(isPrime, new S(N, U_2)), U_3));
								//	new R(new S(Z, U_1), new S(Sum, new S(isPrime, new S(N, U_1)), U_2));
	
	static IPrimitive nthPrime = new M(new S(Minus, new S(N, U_1), new S(kolPrime, U_2)));

	static IPrimitive pow = new R(One, new S(Mul, U_1, U_3));

	static IPrimitive log = new S(Minus1, new M(new S(Not, new S(Mod, U_1, new S(pow, U_2, U_3)))));
	
	public static void main(String[] args)
	{
		IPrimitive p = nthPrime;//new R(new Zsample(), new Zsample());
		System.out.println("RESULT = " + p.exec(4));
	}
}
