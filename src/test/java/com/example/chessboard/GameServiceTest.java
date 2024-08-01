package com.example.chessboard;

import com.example.chessboard.config.BoardConfig;
import com.example.chessboard.dao.CommandRepository;
import com.example.chessboard.dao.UnitHistoryRepository;
import com.example.chessboard.dao.UnitRepository;
import com.example.chessboard.entity.*;
import com.example.chessboard.service.GameService;
import com.example.chessboard.util.UnitFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private CommandRepository commandRepository;

    @Mock
    private UnitHistoryRepository unitHistoryRepository;

    @Mock
    private UnitFactory unitFactory;

    @Mock
    private BoardConfig boardConfig;

    @Test
    void should_ReturnAllUnits_When_GetUnitsCalled(){
        //given
        List<Unit> expected = List.of(new Archer(), new Archer());
        when(unitRepository.findAll()).thenReturn(expected);

        //when
        List<Unit> actual = gameService.getUnits();

        //then
        assertEquals(expected, actual, "Should return all units");
        verify(unitRepository, times(1)).findAll();
    }

    @Test
    void should_ExecuteMoveCommand_When_CommandIsValid(){
        //given
        Unit unit = new Archer();
        unit.setId(1L);
        unit.setLastCommandTimestamp(System.currentTimeMillis() - 6000);
        Command command = new Command();
        command.setUnitId(1L);
        command.setType(CommandType.MOVE);
        command.setDetails("UP 1");
        when(unitRepository.findById(1L)).thenReturn(Optional.of(unit));

        //when
        gameService.executeCommand(command);

        //then
        verify(unitRepository, times(1)).save(unit);
        verify(commandRepository, times(1)).save(command);

    }

    @Test
    void should_ExecuteShootCommand_When_CommandIsValid() {
        //given
        Unit unit = new Archer();
        unit.setId(1L);
        unit.setLastCommandTimestamp(System.currentTimeMillis() - 11000);
        Command command = new Command();
        command.setUnitId(1L);
        command.setType(CommandType.SHOOT);
        command.setDetails("2 0");
        when(unitRepository.findById(1L)).thenReturn(Optional.of(unit));

        //when
        gameService.executeCommand(command);

        //then
        verify(unitRepository, times(1)).save(unit);
        verify(commandRepository, times(1)).save(command);

    }

    @Test
    void should_ExecuteRandomCommand_When_ValidUnitAndSideProvided() {
        //given
        Unit unit = new Archer();
        unit.setId(1L);
        when(unitRepository.findById(1L)).thenReturn(Optional.of(unit));

        //when
        gameService.executeRandomCommand("white", 1L);

        //then
        verify(unitRepository, times(1)).findById(1L);
        verify(unitRepository, atLeastOnce()).save(unit);

    }

    @Test
    void should_ReturnTrue_When_UnitBelongsToSide() {
        // given
        Unit unit = new Cannon();
        unit.setId(1L);
        unit.setColor("white");
        when(unitRepository.findById(1L)).thenReturn(Optional.of(unit));

        // when
        boolean actual = gameService.isUnitBelongsToSide(1L, "white");

        // then: Weryfikacja wyników
        assertTrue(actual, "Unit should belong to the side");
    }

    @Test
    void should_CreateAndSaveUnits_When_NewGameStarted() {
        //given
        List<Unit> units = List.of(new Archer(), new Transport());
        when(unitFactory.createUnits(4, 2, 5)).thenReturn(units);

        //when
        gameService.newGame();

        //then
        verify(unitRepository, times(1)).deleteAll();
        verify(unitRepository, times(1)).saveAll(units);

    }


}