package com.davis.batchprocessor.repository;

import com.davis.batchprocessor.domain.Client;
import com.davis.batchprocessor.domain.Customer2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Customer2Repository extends JpaRepository<Customer2, Long> {
}
