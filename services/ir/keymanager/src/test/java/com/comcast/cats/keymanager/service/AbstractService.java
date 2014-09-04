/**
 * Copyright 2014 Comcast Cable Communications Management, LLC
 *
 * This file is part of CATS.
 *
 * CATS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CATS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CATS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comcast.cats.keymanager.service;

import java.sql.BatchUpdateException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author thusai000
 *
 */
@org.junit.Ignore
public abstract class AbstractService {
	/**
	 * The EntityManager that should be used for the model tests.
	 */
	protected EntityManager em;
	/**
	 * The EntityManagerFactor that creates the EntityManager used.
	 */
	private EntityManagerFactory emf;

	/**
	 * Before the class is initialized, setup Log4J.
	 */
	@BeforeClass
	public static void setupLogging() {
		org.apache.log4j.BasicConfigurator.configure();
	}

	/**
	 * DBUnit will populate fake data prior to execution.
	 * 
	 * @throws java.lang.Exception
	 *             put here to avoid handling the error conditions, just let the
	 *             test fail
	 */
	@Before
	public void setUp() throws Exception {
		emf = Persistence
				.createEntityManagerFactory("key-manager-pu");
		em = emf.createEntityManager();
		final EntityTransaction xact = em.getTransaction();
		xact.begin();
	}

	/**
	 * Since DBUnit will be clearing the Database before each test commit to the
	 * database to ensure this works as expected.
	 */
	@After
	public void tearDown() throws BatchUpdateException {
		final EntityTransaction xact = em.getTransaction();
		xact.commit();
		em.close();
		emf.close();
		
	}
}
