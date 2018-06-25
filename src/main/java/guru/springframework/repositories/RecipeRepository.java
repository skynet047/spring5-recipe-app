package guru.springframework.repositories;

import guru.springframework.domain.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by jt on 6/13/17.
 */
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

	@Query("select recipe from Recipe recipe left join fetch recipe.properties")
	List<Recipe> findAllWithEagerRelationships();

	@Query("select distinct recipe from Recipe recipe left join fetch recipe.properties")
	List<Recipe> findAllDistinctWithEagerRelationships();
}
