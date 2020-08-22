package nus.cs5223.dto;

import nus.cs5223.vo.PlayerVO;

import java.util.List;

public class JoinGameResDTO {

    List<PlayerVO> existingPlayers;

    Integer n;  // grid size
    Integer k;  // number of treasures
}
