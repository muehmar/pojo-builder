package io.github.muehmar.pojobuilder.snapshottesting;

import static org.assertj.core.api.Assertions.assertThat;

import au.com.origin.snapshots.Snapshot;
import au.com.origin.snapshots.reporters.SnapshotReporter;

public class JUnitSnapshotReporter implements SnapshotReporter {
  @Override
  public boolean supportsFormat(String s) {
    return true;
  }

  @Override
  public void report(Snapshot snapshot, Snapshot snapshot1) {
    assertThat(snapshot1.getBody()).isEqualTo(snapshot.getBody());
  }
}
