/*
 * @(#) UserSpecification.java 2021. 08. 12.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.member.specs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.cooingpop.apiclient.api.member.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 조회시 where 조건에 사용
 * @author 박준영
 **/
@AllArgsConstructor
public class UserSpecification {

	public enum SearchKey {
		EMAIL("email"),
		NAME("name");

		@Getter
		private final String value;

		SearchKey(String value) {
			this.value = value;
		}
	}
	public static Specification<User> searchWith(Map<SearchKey, Object> searchKeyword) {
		return (Specification<User>) ((root, query, builder) -> {
			List<Predicate> predicateList = getPredicateWithSearchKeyword(searchKeyword, root, builder);
			return builder.and(predicateList.toArray(new Predicate[0]));
		});
	}

	private static List<Predicate> getPredicateWithSearchKeyword(Map<SearchKey, Object> searchKeyword, Root<User> root, CriteriaBuilder builder) {
		List<Predicate> predicateList = new ArrayList<>();

		if (searchKeyword.keySet().contains(SearchKey.EMAIL) && searchKeyword.keySet().contains(SearchKey.NAME)) {
			predicateList.addAll(
				Arrays.asList(builder.equal(
					root.get(SearchKey.EMAIL.getValue()),searchKeyword.get(SearchKey.EMAIL)
				), builder.equal(
					root.get(SearchKey.NAME.getValue()),searchKeyword.get(SearchKey.NAME)
				))
			);
		} else if (searchKeyword.keySet().contains(SearchKey.EMAIL)) {
			predicateList.add(builder.equal(
				root.get(SearchKey.EMAIL.getValue()),searchKeyword.get(SearchKey.EMAIL)
			));
		} else if (searchKeyword.keySet().contains(SearchKey.NAME)) {
			predicateList.add(builder.equal(
				root.get(SearchKey.NAME.getValue()),searchKeyword.get(SearchKey.NAME)
			));
		}

		return predicateList;
	}
}
