package br.com.lkm.extrator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lkm.extrator.entity.Subsidiary;

@Repository
public interface SubsidiaryRepository extends JpaRepository<Subsidiary, Integer>{

	Page<Subsidiary> findByCorporationId(Integer id, Pageable pageable);

}
