package com.loapulse;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideOutsideOfPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

/**
 * 모듈 경계 규칙 (docs D24). 모듈이 아직 비어 있어도 규칙이 깨지지 않도록 allowEmptyShould를 켠다.
 */
@AnalyzeClasses(packages = "com.loapulse", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

	private static final String ROOT = "com.loapulse.";

	/** 모듈마다 의존할 수 있는 다른 모듈. 그림에 없는 의존은 금지한다. 모든 모듈은 shared에 의존할 수 있다. */
	private static final Map<String, Set<String>> ALLOWED_DEPENDENCIES = Map.of(
			"collector", Set.of(),
			"market", Set.of("collector"),
			"user", Set.of("collector"),
			"alert", Set.of("collector", "market", "user"),
			"realtime", Set.of("collector", "market", "alert"),
			"shared", Set.of());

	private static final List<String> MODULES = List.copyOf(ALLOWED_DEPENDENCIES.keySet());

	@ArchTest
	static void 다른_모듈은_api_패키지만_참조한다(JavaClasses classes) {
		for (String module : MODULES) {
			if (module.equals("shared")) {
				continue;
			}
			noClasses().that().resideOutsideOfPackage(modulePackage(module))
					.should().dependOnClassesThat(resideInAPackage(modulePackage(module))
							.and(resideOutsideOfPackage(ROOT + module + ".api..")))
					.allowEmptyShould(true)
					.check(classes);
		}
	}

	@ArchTest
	static void 모듈은_의존_방향_그림을_따른다(JavaClasses classes) {
		for (String module : MODULES) {
			String[] forbidden = MODULES.stream()
					.filter(other -> !other.equals(module))
					.filter(other -> !other.equals("shared") || module.equals("shared"))
					.filter(other -> !ALLOWED_DEPENDENCIES.get(module).contains(other))
					.map(ArchitectureTest::modulePackage)
					.toArray(String[]::new);
			noClasses().that().resideInAPackage(modulePackage(module))
					.should().dependOnClassesThat(resideInAnyPackage(forbidden))
					.allowEmptyShould(true)
					.check(classes);
		}
	}

	@ArchTest
	static void 모듈_사이에_순환_의존이_없다(JavaClasses classes) {
		slices().matching(ROOT + "(*)..")
				.should().beFreeOfCycles()
				.allowEmptyShould(true)
				.check(classes);
	}

	private static String modulePackage(String module) {
		return ROOT + module + "..";
	}

}
