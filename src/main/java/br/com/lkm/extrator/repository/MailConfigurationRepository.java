package br.com.lkm.extrator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lkm.extrator.entity.MailConfiguration;

@Repository
public interface MailConfigurationRepository extends JpaRepository<MailConfiguration, Integer>{

	MailConfiguration findByCorporationId(Integer userId);

}
