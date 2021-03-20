package org.cocktailtagsearch.util;

import java.math.BigDecimal;

public class Caster {
	public static int bigDecimalObjToInt(Object obj) {
		return ((BigDecimal)obj).intValue();
	}
}
