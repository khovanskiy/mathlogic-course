
public enum OperationType
{
	IMPLICATION
	{
		@Override
		public String getDelimeter()
		{
			return "->";
		}
		
		@Override
		public boolean isRightAssociative()
		{
			return true;
		}
		
		@Override
		public int getPriority()
		{
			return 1;
		}
	},
	DISJUNCTION
	{
		@Override
		public String getDelimeter()
		{
			return "|";
		}
		
		@Override
		public boolean isRightAssociative()
		{
			return false;
		}
		
		@Override
		public int getPriority()
		{
			return 2;
		}
	},
	CONJUNCTION
	{
		@Override
		public String getDelimeter()
		{
			return "&";
		}
		
		@Override
		public boolean isRightAssociative()
		{
			return false;
		}
		
		@Override
		public int getPriority()
		{
			return 3;
		}
	},
	NEGATION
	{
		@Override
		public String getDelimeter()
		{
			return "!";
		}
		
		@Override
		public boolean isRightAssociative()
		{
			return true;
		}
		
		@Override
		public int getPriority()
		{
			return 4;
		}
	},
	ANY
	{
		@Override
		public String getDelimeter()
		{
			return "@";
		}
		
		@Override
		public boolean isRightAssociative()
		{
			return true;
		}
		
		@Override
		public int getPriority()
		{
			return 4;
		}
	},
	EMPTY
	{
		@Override
		public String getDelimeter()
		{
			return "";
		}

		@Override
		public boolean isRightAssociative()
		{
			return false;
		}

		@Override
		public int getPriority()
		{
			return 5;
		}
	};
	public abstract String getDelimeter();
	public abstract boolean isRightAssociative();
	public abstract int getPriority();
}
