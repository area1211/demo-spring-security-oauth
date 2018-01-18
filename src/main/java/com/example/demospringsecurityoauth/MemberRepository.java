package com.example.demospringsecurityoauth;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource // 내부적으로 Rest API가 만들어진다.
public interface MemberRepository extends PagingAndSortingRepository<Member, Long> {
}
