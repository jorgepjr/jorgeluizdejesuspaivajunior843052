package com.musiccatalog.dto;

import java.time.LocalDateTime;

public class RegionalResponse {
    private int totalProcessados;
    private int totalInseridos;
    private int totalAtualizados;
    private LocalDateTime timestamp;

    public int getTotalAtualizados() {return totalAtualizados;}

    public void setTotalAtualizados(int totalAtualizados) {this.totalAtualizados = totalAtualizados;}

    public int getTotalInseridos() {return totalInseridos;}

    public void setTotalInseridos(int totalInseridos) {this.totalInseridos = totalInseridos;}

    public int getTotalProcessados() {return totalProcessados;}

    public void setTotalProcessados(int totalProcessados) {this.totalProcessados = totalProcessados;}

    public LocalDateTime getTimestamp() {return timestamp;}

    public void setTimestamp(LocalDateTime timestamp) {this.timestamp = timestamp;}
}


