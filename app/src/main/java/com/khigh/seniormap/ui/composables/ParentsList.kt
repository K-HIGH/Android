package com.khigh.seniormap.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.khigh.seniormap.ui.model.Person

/**
 * 부모님 목록을 표시하는 컴포넌트
 */
@Composable
fun ParentsList(
    parents: List<Person>,
    onPersonClick: (Person) -> Unit,
    modifier: Modifier = Modifier
) {
    if (parents.isEmpty()) {
        // 부모님이 없을 때 표시할 내용
        EmptyState(
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(parents) { person ->
                PersonListItem(
                    person = person,
                    onClick = { onPersonClick(person) }
                )
            }
        }
    }
}

/**
 * 부모님이 없을 때 표시하는 빈 상태 컴포넌트
 */
@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "빈 상태",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "아직 등록된 부모님이 없습니다",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "우측 상단의 + 버튼을 눌러 부모님을 추가해보세요",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
} 