import java.util.Arrays;

/**
 * 
 * @author Victor Khovanskiy
 *
 */
public class Program
{
	static void trace(String s)
	{
		System.out.println(s);
	}
	
	static void println(String tag, float... array)
	{
		System.out.print(tag);
		for (int i = 0; i < array.length; ++i)
		{
			System.out.print(array[i]+",");
		}
		System.out.println();
	}
	
	static void trace(float... array)
	{
		for (int i = 0; i < array.length; ++i)
		{
			System.out.print(array[i]+",");
		}
		System.out.println();
	}
	
	static interface IPrimitive
	{
		public float exec(float... array);
	}
	
	static class Zsample implements IPrimitive
	{
		@Override
		public float exec(float... array)
		{
			return 0;
		}
	}
	
	static IPrimitive Z = new Zsample();
	
	static class Nsample implements IPrimitive
	{
		@Override
		public float exec(float... array)
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
		public float exec(float... array)
		{
			if (i >= array.length){return Float.NaN;}
			return array[i];
		}
	}
	
	static IPrimitive U_1 = new U(0, 0);
	static IPrimitive U_2 = new U(0, 1);
	static IPrimitive U_3 = new U(0, 2);
	static IPrimitive U_4 = new U(0, 3);
	
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
		public float exec(float... array)
		{
			float y = array[array.length - 1];
			float[] argv = Arrays.copyOfRange(array, 0, array.length - 1);
			float result = f.exec(argv);
			if (y > 0)
			{
				float[] temp = new float[argv.length + 2];
				System.arraycopy(argv, 0, temp, 0, argv.length);
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
		public float exec(float... array)
		{
			float[] temp = new float[array.length + 1];
			System.arraycopy(array, 0, temp, 0, array.length);
			int y = 0;
			
			while (f.exec(temp) != 0)
			{
				++y;
				temp[temp.length - 1] = y;
			}
			return y;
		}
	}
	
	static IPrimitive S(final IPrimitive... gs)
	{
		return new IPrimitive() {
			@Override
			public float exec(float... array)
			{
				float[] temp = new float[gs.length - 1];
				for (int i = 1; i < gs.length; ++i)
				{
					temp[i - 1] = gs[i].exec(array);
				}
				return gs[0].exec(temp);
			}
		};
	}
	
	//static IPrimitive Sum = new R(U_1,S(N,U_3));
	static IPrimitive Sum = new IPrimitive() {
		
		@Override
		public float exec(float... array) {
			return array[0] + array[1];
		}
	};
	
	static IPrimitive Dec = new R(Z,U_1);

	static IPrimitive Minus = new R(U_1,S(Dec,U_3));

	//static IPrimitive Mul = new R(Z, S(Sum, U_1, U_3) );
	static IPrimitive Mul = new IPrimitive() {
		
		@Override
		public float exec(float... array) {
			return array[0] * array[1];
		}
	};

	//static IPrimitive Div = S(Dec, new M(S(Minus, S(N,U_1), S(Mul, U_2, U_3))));
	static IPrimitive Div = new IPrimitive() {
		
		@Override
		public float exec(float... array) {
			return (array[0] - array[0] % array[1]) / array[1];
		}
	};
	

	//static IPrimitive Mod = S(Minus, U_1, S(Mul, U_2, S(Div, U_1,U_2) ) );
	
	static IPrimitive Mod = new IPrimitive() {
		
		@Override
		public float exec(float... array) {
			return array[0] % array[1];
		}
	};
	

	static IPrimitive One = S(N,Z);

	static IPrimitive Or = new R( S( new R( Z, One), U_1, U_1), S(One, U_1));

	static IPrimitive And = new R(Z, S (new R (Z,One),U_1,U_1) );

	static IPrimitive Not = new R(One, Z);

	static IPrimitive If = new R(Z,One);

	static IPrimitive isPrime = S(And,S(Not,S(new R(Z,S(Or, S(And, S(Not, S(Mod, U_1,S(N,U_2))),
						S(If, U_2) ), U_3) ),U_1,S(Dec,S(Dec,U_1)))),S(If,S(Dec,U_1)));

	static IPrimitive countPrime =  new R(S(Z,U_1), S (Sum, S(isPrime,S(N,U_1)), U_2));
	static IPrimitive nthPrime = new M(S(Minus,U_1, S(countPrime,U_2)));
	static IPrimitive pow = new R(One,S(Mul,U_1,U_3));
	static IPrimitive log = S(Dec, new M(S(Not, S(Mod, U_1, S(pow, U_2, U_3)))));
	static IPrimitive number2 = S(N, One);
	static IPrimitive size = S(log, U_1, number2);
	static IPrimitive maxPrime = S(nthPrime,S(N,S(N,size)));
	static IPrimitive lastPrime = S(nthPrime,S(N,size));
	static IPrimitive top = S(log,U_1,S(nthPrime,S(N,S(size,U_1))));
	static IPrimitive push = S(Mul,S(pow,S(maxPrime,U_1),U_2),S(Mul, U_1, number2));
	static IPrimitive pop = S(Div,U_1,S(Mul, S(pow,S(lastPrime, U_1),S(top, U_1)),number2) );
	static IPrimitive secondTop = S(log, U_1, S(nthPrime, S(size, U_1)));
	static IPrimitive stepNewOne = S(push,U_3,S(N,U_2));
	static IPrimitive stepTwoNext = S(push,S(push,U_3,S(Dec,U_1)),One);
	static IPrimitive step3 = S(push,S(push,S(push,U_3, S(Dec,U_1)),U_1 ),S(Dec,U_2));
	static IPrimitive isPTwo = new R(stepTwoNext, step3);
	static IPrimitive secondStep = S(isPTwo, U_1, U_2, U_3, U_2);
	static IPrimitive firstStep = new R(stepNewOne, S(secondStep, U_1, U_2));
	static IPrimitive isP = new R(stepNewOne,S(secondStep, U_1, U_2, U_3));
	static IPrimitive Nnumber = S(top, U_4);
	static IPrimitive Mnumber = S(secondTop, U_4);
	static IPrimitive getStack = S(pop, S(pop, U_4));
	static IPrimitive step = S(isP, Mnumber, Nnumber, getStack, Mnumber);
	static IPrimitive initialize = S(push, S(push, One, U_1), U_2);
	static IPrimitive steps = new R(initialize, step);
	static IPrimitive akkermanMain = new M(S(Dec, S(size, steps)));
	static IPrimitive akkerman = S(top, S(steps, U_1, U_2, S(akkermanMain, U_1, U_2)));
	
	public static void main(String[] args)
	{
		IPrimitive p = akkerman;
		System.out.println("Result " + p.exec(1,1));
	}
}
