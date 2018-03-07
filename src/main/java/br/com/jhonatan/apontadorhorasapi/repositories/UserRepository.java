package br.com.jhonatan.apontadorhorasapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.jhonatan.apontadorhorasapi.domain.User;

@Repository
public interface  UserRepository extends JpaRepository<User, Integer> {

}
