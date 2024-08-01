package com.example.chessboard;

import com.example.chessboard.config.BoardConfig;
import com.example.chessboard.util.UnitFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.chessboard.entity.Unit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UnitFactoryTest {

    @InjectMocks
    private UnitFactory unitFactory;

    @Mock
    private BoardConfig boardConfig;

    @Test
    void should_CreateCorrectUnits_When_ValidParametersProvided() {
        //given
        when(boardConfig.getWidth()).thenReturn(10);
        when(boardConfig.getHeight()).thenReturn(10);

        //when
        List<Unit> actual = unitFactory.createUnits(2, 2, 2);

        //then
        assertEquals(12, actual.size(), "Expected 12 units to be created");

        for (Unit unit : actual) {
            assertNotNull(unit.getColor(), "Unit color should not be null");
            assertTrue(unit.getX() >= 0 && unit.getX() < 10, "Unit X coordinate should be within board limits");
            assertTrue(unit.getY() >= 0 && unit.getY() < 10, "Unit Y coordinate should be within board limits");
        }

        long uniquePositions = actual.stream()
                .map(unit -> unit.getX() + "," + unit.getY())
                .distinct()
                .count();
        assertEquals(12, uniquePositions, "All units should have unique positions");
    }

    @Test
    void should_CreateCorrectUnitsWithoutCollisions_When_BoardIsSmall(){
        //given
        when(boardConfig.getWidth()).thenReturn(2);
        when(boardConfig.getHeight()).thenReturn(3);

        //when
        List<Unit> actual = unitFactory.createUnits(1, 1, 1);

        //then
        assertEquals(6, actual.size(), "Expected 6 units to be created");

        long uniquePositions = actual.stream()
                .map(unit -> unit.getX() + "," + unit.getY())
                .distinct()
                .count();
        assertEquals(6, uniquePositions, "All units should have unique positions on a small board");
    }

}