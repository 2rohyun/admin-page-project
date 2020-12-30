package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

public class ItemRepositoryTest extends StudyApplicationTests {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void create(){
        Item item = new Item();
        item.setStatus("UNREGISTERED");
        item.setName("애플 노트북");
        item.setTitle("애플 노트북 A100");
        item.setContent("2019-16인");
        //item.setPrice(3000000); todo : convert int to BigDecimal
        item.setBrandName("애플");
        item.setRegisteredAt(LocalDateTime.now());
        item.setCreatedAt(LocalDateTime.now());
        item.setCreatedBy("Partner01");
        //item.setPartnerId(1L);

        Item newItem = itemRepository.save(item);
        Assertions.assertNotNull(newItem);
    }

    @Test
    public void read(){
        Long id = 1L;

        Optional<Item> item = itemRepository.findById(id);

        /* 방법1 : 값이 들어있기 때문에 True 반환
        Assertions.assertTrue(item.isPresent());
        */

        /* 방법2 : print 찍어서 확인하는 방법 */
        item.ifPresent(i->{
            System.out.println("id : " + i);
        });
    }
}
