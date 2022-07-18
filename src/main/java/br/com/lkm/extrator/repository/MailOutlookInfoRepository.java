package br.com.lkm.extrator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lkm.extrator.entity.MailOutlookInfo;

@Repository
public interface MailOutlookInfoRepository extends JpaRepository<MailOutlookInfo, Integer>{

	List<MailOutlookInfo> findByEmail(String email);

}
