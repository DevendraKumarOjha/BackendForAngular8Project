package com.luv2code.ecommerce.config;


import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager; // to expose the categoryId in category list:

    @Autowired //inject the entity Manager in your class
    public MyDataRestConfig(EntityManager theEntityManager){
    entityManager = theEntityManager;
    }


    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT,HttpMethod.POST,HttpMethod.DELETE};
        //disable HTTP methods for Product:PUT,POST,DELETE
        config.getExposureConfiguration().forDomainType(Product.class).withItemExposure(
                ((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
        ).withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));

        //disable HTTP methods for ProductCategory:PUT,POST,DELETE
        config.getExposureConfiguration().forDomainType(ProductCategory.class).withItemExposure(
                ((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
        ).withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));

        //call an internal helper method to expose the categoryIds
        exposeIds(config);

    }

    private void exposeIds(RepositoryRestConfiguration config) {
    //expose categoryIds
     //get a list of all entity class from the EntityManager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        //create an array of entity type:
        List<Class> entityClasses = new ArrayList<>();
        //get the entity types for the entity;

        for(EntityType tempEntityType : entities) {
            entityClasses.add(tempEntityType.getJavaType());
        }
        //expose the entityId for the array of entity/domain types

        Class [] domainType = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainType);

    }
}
