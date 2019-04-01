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
	
	static class Z implements IPrimitive
	{
		@Override
		public int exec(int... array)
		{
			/*if (array.length != 1 || array[0] < 0)
			{
				throw new IllegalArgumentException();
			}*/
			return 0;
		}
	}
	
	static class N implements IPrimitive
	{
		@Override
		public int exec(int... array)
		{
			if (array.length != 1 || array[0] < 0)
			{
				throw new IllegalArgumentException();
			}
			return array[0] + 1;
		}
	}
	
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
			int y = array[0];
			if (y == 0)
			{
				return f.exec(Arrays.copyOfRange(array, 1, array.length));
			}
			else
			{
				int[] tempA = array.clone();
				tempA[0] = y - 1;
				int[] tempB = new int[array.length + 1];
				tempB[0] = exec(tempA);
				System.arraycopy(tempA, 0, tempB, 1, tempA.length);
				return g.exec(tempB);
			}
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
	
	static class Mux implements IPrimitive
	{
		private static IPrimitive g = new R(new U(2,1), new U(4,2));
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	static class Pred implements IPrimitive
	{
		private static IPrimitive g = new S(new R(new Z(), new U(3,1)), new U(1,0), new Z());

		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
		
	}
	
	static class SubRev implements IPrimitive
	{
		private static IPrimitive g = new R(new U(1,0), new S(new Pred(), new U(3,0)));
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	static class isZero implements IPrimitive
	{
		private static IPrimitive g = new S(new R(new S(new N(), new Z()), new S(new Z(), new U(3,0))), new U(1,0), new Z());
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	static class Le implements IPrimitive
	{
		private static IPrimitive g = new S(new isZero(), new Sub());
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	static class Ge implements IPrimitive
	{
		private static IPrimitive g = new S(new isZero(), new SubRev());
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	static class Eq implements IPrimitive
	{
		private static IPrimitive g = new S(new isZero(), new S(new Plus(), new Sub(), new SubRev()));
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	static class Pow implements IPrimitive
	{
		private static IPrimitive g = new S(new R(new S(new N(), new Z()), new S(new Mul(), new U(3,2), new U(3,0))), new U(2,1), new U(2,0));
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	static class Even implements IPrimitive
	{
		private static IPrimitive g = new S(new R(new S(new N(), new Z()), new S(new isZero(), new U(3,0))), new U(1,0), new Z());
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	// 1) Сложение
	static class Plus implements IPrimitive
	{
		private static IPrimitive g = new R(new U(1,0), new S(new N(), new U(3,0)));
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	// 2) Ограниченное вычитание
	static class Sub implements IPrimitive
	{
		private static IPrimitive g = new S(new SubRev(), new U(2,1), new U(2,0));
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	// 3) Умножение
	static class Mul implements IPrimitive
	{
		private static IPrimitive g = new R(new Z(), new S(new Plus(), new U(3, 0), new U(3, 2)));
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	// 4) Целочисленное деление
	static class Div implements IPrimitive
	{
		//private static IPrimitive g = new S(new R(new S(new Z(), new U(2,0)), new S(new Mux(), new S(new Le(), new S(new Mul(), new S(new N(), new U(4,0)), new U(4,3)), new U(4,2)), new S(new N(), new U(4,0)), new U(4,0))), new U(2,0), new U(2,0), new U(2,1));
		private static IPrimitive g = new M(new S(new Sub(), new S(new N(),new U(3,0)), new S(new Mul(), new U(3,1),new S(new N(), new U(3,2)))));
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	// 5) Остаток от деления
	static class Mod implements IPrimitive
	{
		private static IPrimitive g = new S(new R(new U(2,0), new S(new Mux(), new S(new Ge(), new U(4,0), new U(4,3)), new S(new Sub(), new U(4,0), new U(4,3)), new U(4,0))), new U(2,0), new U(2,0), new U(2,1));
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	// 6) Проверка на простоту
	static class isPrime implements IPrimitive
	{
		private static IPrimitive g = new S(new Eq(), new S(new R(new Z(), new S(new Plus(), new S(new S(new isZero(), new Mod()), new U(3,2), new U(3,1)), new U(3,0))), new U(1,0), new U(1,0)), new S(new N(), new Z()));
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	// 7) n-ое простое число
	static class nthPrime implements IPrimitive
	{
		private static IPrimitive g = new S(new Mux(), new U(1,0), new S(new R(new Z(), new S(new Mux(), new S(new Even(), new U(3,0)), new S(new Mux(), new S(new isPrime(), new U(3,1)), new S(new Mux(), new S(new Eq(), new U(3,0), new S(new Plus(), new U(3,2), new U(3,2))), new U(3,1), new S(new N(), new S(new N(), new U(3,0)))), new U(3,0)), new U(3,0))), new S(new Pow(), new S(new N(), new S(new N(), new Z())), new S(new N(), new U(1,0))), new U(1,0)), new S(new N(), new S(new N(), new Z())));
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	// 8) Логарифмирование
	static class pLog implements IPrimitive
	{
		private static IPrimitive g = new S(new R(new S(new Z(), new U(2,0)), new S(new Mux(), new S(new Le(), new S(new Pow(), new U(4,2), new S(new N(), new U(4,0))), new U(4,3)), new S(new N(), new U(4,0)), new U(4,0))), new U(2,1), new U(2,0), new U(2,1));
		//private static IPrimitive g = new S(new R(new Z(), new U(1, 0)), new M( new S( new R(new S(new N(), new Z()), new Z()), new S(new S(new R(new U(1, 0), new S(new R(new Z(),new U(1, 0)), new U(1, 2))), new U(1, 0), new S(new Mul(), new U(1, 1), new S(new Div(), new U(1, 0), new U(1, 1)))), new U(1, 0), new S(new R(new S(new N(), new Z()), new S(new Mul(), new U(1, 0), new U(1, 2))), new U(1, 1), new U(1, 2))))));
		//private static IPrimitive g = new S(new R(new Z(), new U(1,0)), new M(new S(new R(new S(new N(), new Z()), new Z()), new S(new Mod(), new U(1,0), new S(new Pow(), new U(1,1), new U(1,2))))));
		
		@Override
		public int exec(int... array)
		{
			return g.exec(array);
		}
	}
	
	public static void main(String[] args)
	{
		IPrimitive p = new pLog();
		System.out.println("RESULT = " + p.exec(5, 7));
	}
}
