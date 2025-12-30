package com.poncegl.sigc.ui.feature.patients.domain.model

enum class MedicationUnit(val label: String) {
    PZAS("pzas"),
    AMPS("amps"),
    ML("mL"),
    G("g"),
    DOSE("dosis"),
    UNITS("unidades"),
}

enum class MedicationPresentation(val label: String) {
    TABLET("Pastilla/Comprimido"),
    CAPSULE("Cápsula"),
    SYRUP("Jarabe"),
    SUSPENSION("Suspensión"),
    DROPS("Gotas"),
    INJECTION("Inyección"),
    CREAM("Crema"),
    LOTION("Loción"),
    OINTMENT("Pomada/Ungüento"),
    GEL("Gel"),
    INHALER("Inhalador"),
    POWDER("Polvo"),
    SUPPOSITORY("Supositorio"),
    SPRAY("Spray"),
    OTHER("Otro");

    /**
     * Devuelve la configuración de etiquetas y unidades para el formulario de inventario
     * basándose en la presentación seleccionada (Context-Aware UI).
     */
    fun getInventoryConfig(): InventoryConfig {
        return when (this) {
            // Sólidos contables
            TABLET, CAPSULE, SUPPOSITORY -> InventoryConfig(
                contentLabel = "Piezas por caja",
                containerLabel = "Cajas",
                defaultUnit = MedicationUnit.PZAS.label
            )
            // Inyectables (Suelen venir en ampolletas dentro de cajas)
            INJECTION -> InventoryConfig(
                contentLabel = "Ampolletas por caja",
                containerLabel = "Cajas",
                defaultUnit = MedicationUnit.AMPS.label
            )
            // Líquidos (La unidad es el envase entero)
            SYRUP, SUSPENSION, LOTION, DROPS -> InventoryConfig(
                contentLabel = "Contenido del envase",
                containerLabel = "Envases",
                defaultUnit = MedicationUnit.ML.label
            )
            // Semisólidos (Tubos o tarros)
            CREAM, OINTMENT, GEL, POWDER -> InventoryConfig(
                contentLabel = "Contenido del tubo/tarro",
                containerLabel = "Piezas",
                defaultUnit = MedicationUnit.G.label
            )
            // Dosis medidas
            INHALER, SPRAY -> InventoryConfig(
                contentLabel = "Dosis por unidad",
                containerLabel = "Unidades",
                defaultUnit = MedicationUnit.DOSE.label
            )
            // Fallback genérico
            OTHER -> InventoryConfig(
                contentLabel = "Contenido por empaque",
                containerLabel = "Empaques",
                defaultUnit = MedicationUnit.UNITS.label
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
    val defaultUnit: String
)