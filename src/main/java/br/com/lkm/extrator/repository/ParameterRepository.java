package br.com.lkm.extrator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lkm.extrator.entity.Parameter;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Integer>{

	List<Parameter> findByName(String name);

}
