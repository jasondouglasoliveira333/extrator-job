package br.com.lkm.extrator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lkm.extrator.entity.MailInfo;

@Repository
public interface MailInfoRepository extends JpaRepository<MailInfo, Integer>{

}
