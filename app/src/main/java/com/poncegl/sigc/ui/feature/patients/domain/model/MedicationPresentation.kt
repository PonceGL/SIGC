package com.poncegl.sigc.ui.feature.patients.domain.model

enum class MedicationUnit(val label: String) {
    PZAS("pzas"),
    AMPS("amps"),
    ML("mL"),
    G("g"),
    DOSE("dosis"),
    UNITS("unidades"),
    ENVASES("envases")
}

/**
 * Define cómo se comporta el inventario matemáticamente.
 */
enum class StockStrategy {
    /**
     * Deductivo: El stock disminuye con cada toma registrada (Dosis x Frecuencia).
     * Ej: Pastillas, Jarabes medidos.
     * Alerta: Basada en cálculo de días restantes.
     */
    CALCULATED,

    /**
     * Gestión por Envase: El stock disminuye solo por evento manual ("Terminé el envase").
     * Ej: Cremas, Sprays, Gotas oftálmicas.
     * Alerta: Basada en cantidad de envases cerrados (Reserva).
     */
    BY_CONTAINER
}

enum class MedicationPresentation(val label: String, val strategy: StockStrategy) {
    // Estrategia: CALCULATED (Restamos dosis exacta)
    TABLET("Pastilla/Comprimido", StockStrategy.CALCULATED),
    CAPSULE("Cápsula", StockStrategy.CALCULATED),
    SYRUP("Jarabe", StockStrategy.CALCULATED),
    SUSPENSION("Suspensión", StockStrategy.CALCULATED),
    DROPS("Gotas (Oráles/Medibles)", StockStrategy.CALCULATED),
    INJECTION("Inyección", StockStrategy.CALCULATED),
    SUPPOSITORY("Supositorio", StockStrategy.CALCULATED),
    POWDER("Polvo (Sobres/Medido)", StockStrategy.CALCULATED),

    // Estrategia: BY_CONTAINER (Gestionamos envases)
    CREAM("Crema", StockStrategy.BY_CONTAINER),
    LOTION("Loción", StockStrategy.BY_CONTAINER),
    OINTMENT("Pomada/Ungüento", StockStrategy.BY_CONTAINER),
    GEL("Gel", StockStrategy.BY_CONTAINER),
    INHALER("Inhalador", StockStrategy.BY_CONTAINER),
    SPRAY("Spray", StockStrategy.BY_CONTAINER),

    OTHER("Otro", StockStrategy.BY_CONTAINER);

    /**
     * Devuelve la configuración de etiquetas y unidades para el formulario de inventario
     * basándose en la presentación seleccionada (Context-Aware UI).
     */
    fun getInventoryConfig(): InventoryConfig {
        return when (this) {
            // Sólidos (Cajas con piezas)
            TABLET, CAPSULE, SUPPOSITORY, POWDER -> InventoryConfig(
                contentLabel = "Piezas por caja",
                containerLabel = "Cajas disponibles",
                defaultUnit = MedicationUnit.PZAS.label,
                inputStrategy = StockStrategy.CALCULATED
            )
            // Líquidos/Inyectables (Cajas con ampolletas o mL)
            INJECTION -> InventoryConfig(
                contentLabel = "Ampolletas por caja",
                containerLabel = "Cajas disponibles",
                defaultUnit = MedicationUnit.AMPS.label,
                inputStrategy = StockStrategy.CALCULATED
            )

            SYRUP, SUSPENSION, DROPS -> InventoryConfig(
                contentLabel = "Contenido del envase (mL)",
                containerLabel = "Envases disponibles",
                defaultUnit = MedicationUnit.ML.label,
                inputStrategy = StockStrategy.CALCULATED
            )
            // Semisólidos/Difíciles de medir (Gestión por envase)
            CREAM, OINTMENT, GEL, LOTION, INHALER, SPRAY, OTHER -> InventoryConfig(
                contentLabel = "Tamaño del envase (Informativo)",
                containerLabel = "Envases disponibles",
                defaultUnit = "",
                inputStrategy = StockStrategy.BY_CONTAINER
            )
        }
    }
}

/**
 * Helper data class para transportar la configuración de UI
 */
data class InventoryConfig(
    val contentLabel: String,
    val containerLabel: String,
    val defaultUnit: String,
    val inputStrategy: StockStrategy
)