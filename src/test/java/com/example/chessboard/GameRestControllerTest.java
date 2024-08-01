package com.example.chessboard;

import com.example.chessboard.entity.Archer;
import com.example.chessboard.entity.Command;
import com.example.chessboard.entity.Transport;
import com.example.chessboard.entity.Unit;
import com.example.chessboard.rest.GameRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.chessboard.service.GameService;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GameRestControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameRestController gameRestController;

    private List<Unit> units;

    @BeforeEach
    void setUp() {
        units = List.of(new Archer(), new Transport());
    }
    @Test
    void should_ReturnUnits_When_GetUnitsCalled() {
        // given
        when(gameService.getUnits()).thenReturn(units);

        // when
        List<Unit> actual = gameRestController.getUnits();

        // then
        assertEquals(units, actual);
        verify(gameService, times(1)).getUnits();
    }

    @Test
    void should_ExecuteCommand_When_PostCommandCalled() {
        // given
        Command command = new Command();

        // when
        gameRestController.executeCommand(command);

        // then
        verify(gameService, times(1)).executeCommand(command);
    }

    @Test
    void should_ReturnBadRequest_When_UnitDoesNotBelongToSide() {
        // given
        String side = "white";
        Long unitId = 1L;
        when(gameService.isUnitBelongsToSide(unitId, side)).thenReturn(false);

        // when
        ResponseEntity<Void> actual = gameRestController.executeRandomCommand(side, unitId);

        // then
        assertEquals(ResponseEntity.badRequest().build(), actual);
        verify(gameService, times(1)).isUnitBelongsToSide(unitId, side);
        verify(gameService, times(0)).executeRandomCommand(anyString(), anyLong());
    }

    @Test
    void should_ExecuteRandomCommand_When_UnitBelongsToSide() {
        // given
        String side = "white";
        Long unitId = 1L;
        when(gameService.isUnitBelongsToSide(unitId, side)).thenReturn(true);

        // when
        ResponseEntity<Void> actual = gameRestController.executeRandomCommand(side, unitId);

        // then
        assertEquals(ResponseEntity.ok().build(), actual);
        verify(gameService, times(1)).isUnitBelongsToSide(unitId, side);
        verify(gameService, times(1)).executeRandomCommand(side, unitId);
    }

    @Test
    void should_StartNewGame_When_PostNewGameCalled() {
        // when
        gameRestController.newGame();

        // then
        verify(gameService, times(1)).newGame();
    }

}