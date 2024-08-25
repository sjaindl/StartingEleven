package com.sjaindl.s11.faq

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.baseui.expandable.ExpandableCard
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.firestore.faq.model.Faq
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Faqs(
    faqState: FaqState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (faqState) {
        FaqState.Initial, FaqState.Loading -> {
            LoadingScreen()
        }

        is FaqState.Error -> {
            ErrorScreen(
                modifier = modifier,
                text = faqState.message,
                onButtonClick = onRetry,
            )
        }

        is FaqState.Content -> {
            FAQSection(faqs = faqState.faqs, modifier = modifier)
        }
    }
}

@Composable
fun FAQSection(
    faqs: List<Faq>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = modifier,
    ) {
        items(faqs) { faq ->
            ExpandableFAQCard(
                title = faq.question,
                description = faq.answer,
            )
        }
    }
}

@Composable
fun ExpandableFAQCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    ExpandableCard(
        title = title,
        modifier = modifier,
    ) {
        Text(
            text = description,
            modifier = Modifier
                .padding(bottom = 8.dp),
            style = typography.bodySmall.copy(
                color = colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
            )
        )
    }
}

@Preview
@Composable
fun FaqsPreview() {
    val faqs = listOf(
        Faq(
            question = "Wie kann ich teilnehmen? ",
            answer = "Beim ersten Einsteigen ist eine Anmeldung notwendig. Mit dieser Anmeldung kann man sich im Anschluss anmelden und die persönlichen Daten bleiben gespeichert.",
        ),
        Faq(
            question = "Was ist das Ziel des Spiels?",
            answer = "Ziel ist es, am Ende der Saison am meisten Punkte zu erhalten. Die Reihenfolge entsteht nach den tatsächlich erreichten Punkten aller Runden und von den 11 aufgestellten Spielern des jeweiligen Users.",
        ),
        Faq(
            question = "Um was geht es?",
            answer = "Ziel des Spiels soll sein, dass man mit Spaß, Punkten, Ranglisten und Duellen den Verein und die Spieler näher kennenlernt. Die Teilnahme ist völlig KOSTENLOS. Einer Teilnahme steht somit nichts mehr im Weg. Meldet euch an, spielt mit, probiert es aus, seid dabei, macht euch selber ein Bild und eine Meinung!",
        ),
    )
    HvtdpTheme {
        Faqs(
            faqState = FaqState.Content(faqs = faqs),
            onRetry = { },
        )
    }
}
