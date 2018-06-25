package guru.springframework.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class RecipeAdditionalProperty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	public RecipeAdditionalProperty() {
	}

	public RecipeAdditionalProperty(String name) {
		this.name = name;
	}
}
