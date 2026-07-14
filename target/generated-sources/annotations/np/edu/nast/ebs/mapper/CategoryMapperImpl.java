package np.edu.nast.ebs.mapper;

import javax.annotation.processing.Generated;
import np.edu.nast.ebs.dto.request.CategoryRequestDTO;
import np.edu.nast.ebs.dto.response.CategoryResponseDTO;
import np.edu.nast.ebs.model.Category;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-14T08:53:04+0545",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CategoryRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Category category = new Category();

        category.setDescription( dto.getDescription() );
        category.setName( dto.getName() );

        return category;
    }

    @Override
    public CategoryResponseDTO toDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

        categoryResponseDTO.setCategoryId( category.getCategoryId() );
        categoryResponseDTO.setDescription( category.getDescription() );
        categoryResponseDTO.setName( category.getName() );

        return categoryResponseDTO;
    }
}
