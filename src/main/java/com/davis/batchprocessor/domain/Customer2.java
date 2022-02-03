/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.davis.batchprocessor.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Michael Minella
 */
@Entity
@Table(name = "customer2")
public class Customer2 extends  CustomerRegister {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@Column(name = "firstName")
	private String firstName;
	//@Column(name = "middleInitial")
	private String middleInitial;
	private String tipoReg;
//	private List<Transaction> transactions;

	public Customer2() {
	}

	public Customer2(String firstName, String middleInitial, String tipoReg) {
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.tipoReg = tipoReg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}


	public String getTipoReg() {
		return tipoReg;
	}

	public void setTipoReg(String tipoReg) {
		this.tipoReg = tipoReg;
	}

	@Override
	public String toString() {
		return "Customer2{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", middleInitial='" + middleInitial + '\'' +
				", tipoReg='" + tipoReg + '\'' +
				'}';
	}
}
