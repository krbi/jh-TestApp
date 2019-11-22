package com.mycompany.mytestapp.repository;
import com.mycompany.mytestapp.domain.Temat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Temat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TematRepository extends JpaRepository<Temat, Long> {

}
