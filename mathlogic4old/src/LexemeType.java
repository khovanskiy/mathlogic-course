
public enum LexemeType
{
	DOT
	{
		@Override
		public String getDelimeter()
		{
			return ",";
		}
		
		@Override
		public boolean isRightAssociative()
		{
			return false;
		}
		
		@Override
		public int getPriority()
		{
			return 1;
		}
	},
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
	OPEN_BRACKET
	{
		@Override
		public String getDelimeter()
		{
			return "(";
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
	CLOSE_BRACKET
	{
		@Override
		public String getDelimeter()
		{
			return "(";
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
			return 6;
		}
	},
	EXISTS
	{
		@Override
		public String getDelimeter()
		{
			return "?";
		}
		
		@Override
		public boolean isRightAssociative()
		{
			return true;
		}
		
		@Override
		public int getPriority()
		{
			return 6;
		}
	},
	PREDICATE
	{
		@Override
		public String getDelimeter()
		{
			return "";
		}
		
		@Override
		public boolean isRightAssociative()
		{
			return true;
		}
		
		@Override
		public int getPriority()
		{
			return 6;
		}
	},
	VARIABLE
	{
		@Override
		public String getDelimeter()
		{
			return "";
		}
		
		@Override
		public boolean isRightAssociative()
		{
			return true;
		}
		
		@Override
		public int getPriority()
		{
			return 6;
		}
	},
	FUNCTION
	{
		@Override
		public String getDelimeter()
		{
			return "";
		}
		
		@Override
		public boolean isRightAssociative()
		{
			return true;
		}
		
		@Override
		public int getPriority()
		{
			return 6;
		}
	},
	SUBJECT
	{
		@Override
		public String getDelimeter()
		{
			return "";
		}
		
		@Override
		public boolean isRightAssociative()
		{
			return true;
		}
		
		@Override
		public int getPriority()
		{
			return 6;
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
			return 7;
		}
	};
	public abstract String getDelimeter();
	public abstract boolean isRightAssociative();
	public abstract int getPriority();
}
