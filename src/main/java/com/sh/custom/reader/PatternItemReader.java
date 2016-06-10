package com.sh.custom.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.sh.model.Person;

public class PatternItemReader implements ItemReader<Person>{

	@Override
	public Person read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		
		
		return null;
	}
	
}
