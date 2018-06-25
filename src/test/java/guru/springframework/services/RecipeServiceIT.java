package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Created by jt on 6/21/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceIT {

    public static final String NEW_DESCRIPTION = "New Description";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Autowired
    private EntityManager em;

    @Transactional
    @Test
    public void testSaveOfDescription() throws Exception {
        //given
        Iterable<Recipe> recipes = recipeRepository.findAll();
        Recipe testRecipe = recipes.iterator().next();
        RecipeCommand testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);

        //when
        testRecipeCommand.setDescription(NEW_DESCRIPTION);
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(testRecipeCommand);

        //then
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
        assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
        assertEquals(testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
    }

    @Transactional
    @Test
    public void testDistinctRoot(){

        Session session = em.unwrap(Session.class);

        int recipseFromFindAllWithEagerSize = recipeRepository.findAllWithEagerRelationships().size();
        int recipesFromCriteriaDefaultSize =  session.createCriteria(Recipe.class)
                .setFetchMode("properties", FetchMode.JOIN)
                .list().size();

        Criteria criteria = session.createCriteria(Recipe.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        int recipesFromCriteriaDistinctRootEntitySize = criteria.list().size();
        int recipesFromFindAllSize = (int)recipeRepository.findAll().spliterator().getExactSizeIfKnown();
        int recipseFromFindAllDistinctWithEagerSize = recipeRepository.findAllDistinctWithEagerRelationships().size();

        //then
        assertEquals(recipesFromCriteriaDistinctRootEntitySize, recipesFromFindAllSize );
        assertEquals(recipesFromCriteriaDefaultSize, recipseFromFindAllWithEagerSize);
        assertEquals(recipesFromFindAllSize, recipseFromFindAllDistinctWithEagerSize);


    }
}
